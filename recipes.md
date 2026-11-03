# Recipes

**Tested combinations. Each one says what the lab proved**
**and what it merely reasoned about.**

## 1. A vault in a consumer cloud folder

Create the vault (desktop app) inside the folder your sync
client watches; add files through the mounted vault; lock
it when you stop working. The provider stores and syncs
ciphertext only.

*Proven here:* the vault folder is self-contained and
portable (t08); names and contents are absent from it
(t06). *Your part:* the desktop create and the sync client
itself, which no sandbox can run for you.

## 2. A headless server vault, scripted

On a server with no desktop: install the CLI zip, export
the password from the environment (populated by your
secret store at run time, never written into the script),
unlock through the fallback mounter on a loopback port,
and `curl` files in and out over WebDAV. End the process
to lock.

*Proven here:* every step of this recipe is transcripts
t04–t07 verbatim, including the byte-identical round
trips. *Mind:* loopback only — see Chapter 6's working
rules.

## 3. The pre-upload check

Before trusting any new vault arrangement, stand outside
it: copy a distinctive sentence into a file in the vault,
lock it, then grep the vault folder for the sentence, the
filename and a common word from the text. Silence is the
passing grade.

*Proven here:* transcript t06, four greps, four silences.

## 4. Prove your backup before you need it

On a schedule: copy the *backup* of the vault to a scratch
folder, unlock the copy, read one known file, compare its
hash with the known-good value, lock, delete the copy.
Automating recipe 2's steps turns this into a cron-sized
script.

*Proven here:* the copy-unlock-read manoeuvre is t08;
tamper evidence is t10 — if the backup copy were damaged,
the read is where you'd find out, with an authentication
failure rather than silent corruption.

## 5. Move a vault to a new machine

Copy the vault folder by any means (sync, stick, `rsync`),
install Cryptomator on the new machine, open the folder as
an existing vault, type the password. There is no step 3.
Format 8 vaults open in the desktop apps, the mobile apps
and the CLI alike.

*Proven here:* the lab's vault was created by one program
(the library helper) and opened by another (the official
CLI) — the same cross-tool compatibility, exercised.

## 6. Change a shared vault's password after a member leaves

Desktop app: vault options → change password → distribute
the new password to the remaining members through your
password manager's sharing. Remember what this does *not*
do: anyone who kept a copy of the old vault folder and
knows the old password can still open *that copy* —
password changes protect the vault going forward, not
copies already taken.

*Proven here:* the re-wrap mechanics and both-way proof
are t11. The distribution is people process, not
software.

**Reader check:** each recipe should read as assembly, not
discovery — if a step feels new, its chapter is the one to
revisit (recipe 2 is Chapters 5–6, recipe 3 is Chapter 7,
recipe 6 is Chapter 8).
