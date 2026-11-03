# Glossary

The plain words, defined once. Key terms are also defined
where they first appear in the chapters.

**Vault** — A folder Cryptomator manages: ciphertext on
disk, cleartext when unlocked. Also the abstract "place"
your files live, across all your machines.

**Vault folder** — The actual folder on disk holding
`masterkey.cryptomator`, `vault.cryptomator` and the `d/`
tree. The thing sync tools and backups copy.

**Unlock / lock** — Unlocking unwraps the masterkey with
your password and presents the cleartext through a mounter;
locking ends that presentation. With the CLI, the unlock
process *is* the unlocked vault — stopping it locks.

**Mounter** — The component that presents an unlocked
vault: a FUSE mount, a GIO mount, or a WebDAV server (the
"fallback mounter"). The files are the same through every
door.

**Masterkey** — The vault's randomly generated root key.
Every file key traces back to it. Never stored in the
clear; stored wrapped in `masterkey.cryptomator`.

**masterkey.cryptomator** — The small JSON file holding
the masterkey wrapped under a key derived from your
password (plus the scrypt parameters and a version check
value). The crown jewels: back it up with the vault, guard
it like the vault.

**vault.cryptomator** — The signed config token (a JWT)
pinning the vault's format version (8), cipher combination
(SIV_CTRMAC) and filename-shortening threshold. Signed with
the masterkey, so token and masterkey must belong to the
same vault.

**Scrypt** — The deliberately expensive password-stretching
function that turns your password into the wrapping key.
Its cost is why guessing passwords is slow even with the
masterkey file in hand.

**Pepper** — An optional extra secret mixed into key
derivation in some deployments. The standalone tools use
an empty pepper — verified in this lab against the CLI's
own code.

**Per-file encryption** — Each file gets its own random
file key (kept in the file's header, protected by the
masterkey). Consequences: cheap incremental sync, and
damage confined to the file damaged.

**Filename encryption** — Names are encrypted with AES-SIV
and shown as base64url `.c9r` names. SIV mode is
deterministic, so the same name in the same folder maps to
the same ciphertext name without revealing the name.

**AES-SIV / SIV_CTRMAC** — The vault's cipher combination:
SIV mode for filenames (and directory IDs), and CTR mode
with HMAC authentication for file contents, chunk by chunk
— which is why a flipped byte fails authentication instead
of quietly decrypting to garbage.

**Chunk** — The unit in which file contents are encrypted
and authenticated. Corruption is detected per chunk:
"Authentication of chunk 0 failed" names the chunk, and
only that file is condemned.

**Directory ID** — A random identifier per folder inside
the vault. A folder's contents live in the `d/` tree under
a hash of its ID, dissolving your folder structure.

**d/ tree** — The two-level bucket layout (`d/XX/REST/`)
holding every encrypted file and folder link in the vault.

**.c9r** — The filename extension of everything encrypted
in a vault: files, directory-ID files (`dirid.c9r`), and
folder links (`dir.c9r` inside a `.c9r`-named directory).

**Recovery key** — A long string the desktop app can issue
at vault creation that unwraps the masterkey independently
of the password, for resetting a forgotten one. As powerful
as the password; store it like one.

**WebDAV door** — This guide's name for the fallback
mounter: the unlocked vault served over HTTP/WebDAV on
localhost, readable by `curl` and file managers. Loopback
only; the random path is a convenience, not a lock.

**FUSE** — The kernel interface that lets a program present
a filesystem as a mounted directory. Vaults normally mount
through it; the lab sandbox has no `/dev/fuse`, which is
why the WebDAV door carries this guide's lab.

**Format 8** — The current vault format version, pinned in
the config token. All current Cryptomator apps read it;
older releases that predate it cannot.
