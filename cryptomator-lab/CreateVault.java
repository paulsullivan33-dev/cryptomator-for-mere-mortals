import org.cryptomator.cryptolib.api.CryptorProvider;
import org.cryptomator.cryptolib.api.Masterkey;
import org.cryptomator.cryptolib.common.MasterkeyFileAccess;
import org.cryptomator.cryptofs.CryptoFileSystemProperties;
import org.cryptomator.cryptofs.CryptoFileSystemProvider;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

/**
 * Creates a brand-new Cryptomator vault (format 8) in an empty directory,
 * using only the official org.cryptomator libraries that ship inside the
 * Cryptomator CLI 0.6.2 distribution (cryptolib 2.2.0, cryptofs 2.8.0).
 *
 * The current CLI can unlock vaults but cannot create them, so this helper
 * performs the same two steps the desktop app performs when it creates a
 * vault:
 *   1. Write masterkey.cryptomator: a fresh random masterkey, wrapped with
 *      a key derived from the passphrase via scrypt (MasterkeyFileAccess,
 *      constructed with the same empty pepper the CLI itself uses).
 *   2. CryptoFileSystemProvider.initialize(...): cryptofs creates a fresh
 *      vault config, writes it as the signed vault.cryptomator token, and
 *      creates the encrypted root directory under d/. The key loader hands
 *      it the masterkey by loading the file written in step 1, exactly as
 *      an unlock would.
 *
 * Classpath note: put the distribution's library jars (lib/app/mods/*.jar)
 * on the classpath EXCEPT cryptomator-cli-0.6.2.jar itself. The app jar
 * carries a logging configurator compiled for a newer Java than 21 and
 * crashes older runtimes the moment anything logs; the libraries do not
 * need it.
 *
 * Usage: java -cp <library jars> CreateVault <vaultDir> <passphrase>
 * The passphrase is a throwaway lab passphrase passed as an argument so the
 * transcript shows it; never do this with a real vault passphrase.
 */
public class CreateVault {
	public static void main(String[] args) throws Exception {
		Path vaultDir = Paths.get(args[0]);
		String passphrase = args[1];
		Files.createDirectories(vaultDir);

		SecureRandom rng = new SecureRandom();
		MasterkeyFileAccess fileAccess = new MasterkeyFileAccess(new byte[0], rng);
		Path masterkeyPath = vaultDir.resolve("masterkey.cryptomator");
		try (Masterkey masterkey = Masterkey.generate(rng)) {
			fileAccess.persist(masterkey, masterkeyPath, passphrase);
			System.out.println("wrote " + masterkeyPath.getFileName() + " (scrypt-wrapped masterkey)");
		}

		var properties = CryptoFileSystemProperties.cryptoFileSystemProperties()
				.withKeyLoader(uri -> fileAccess.load(masterkeyPath, passphrase))
				.withCipherCombo(CryptorProvider.Scheme.SIV_CTRMAC)
				.withShorteningThreshold(220)
				.build();
		CryptoFileSystemProvider.initialize(vaultDir, properties, URI.create("masterkeyfile:masterkey.cryptomator"));
		System.out.println("initialized vault contents (vault.cryptomator token + encrypted root under d/)");
	}
}
