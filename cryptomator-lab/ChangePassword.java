import org.cryptomator.cryptolib.common.MasterkeyFileAccess;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

/**
 * Changes a vault's passphrase the way the desktop app does: the masterkey
 * itself does not change, only its wrapping in masterkey.cryptomator is
 * redone under a key derived from the new passphrase
 * (MasterkeyFileAccess.changePassphrase, official cryptolib 2.2.0).
 *
 * Usage: java -cp <library jars> ChangePassword <masterkeyFile> <old> <new>
 * Throwaway lab passphrases as arguments, as in CreateVault.
 */
public class ChangePassword {
	public static void main(String[] args) throws Exception {
		Path masterkeyFile = Paths.get(args[0]);
		byte[] before = Files.readAllBytes(masterkeyFile);
		MasterkeyFileAccess access = new MasterkeyFileAccess(new byte[0], new SecureRandom());
		byte[] after = access.changePassphrase(before, args[1], args[2]);
		Files.write(masterkeyFile, after);
		System.out.println("passphrase changed for " + masterkeyFile);
	}
}
