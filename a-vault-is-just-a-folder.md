# A Vault Is Just a Folder

**Everything Cryptomator promises about syncing, sharing**
**and backup rests on one humble fact: a vault is a folder**
**of files, and copying the folder copies the vault.** The
lab proves it before asking you to build habits on it.

## The proof (transcript t08)

```text
$ cp -r vault vault-copy
$ cryptomator-cli unlock ... vault-copy
[main] INFO o.c.cli.Unlock - Unlocked and mounted vault
  successfully to http://localhost:18091/<a new UUID>
$ curl ... /field-notes.txt
sha256: identical to the original fixture
```

A plain recursive copy, unlocked with the same password,
returned the file byte-for-byte. No export, no re-encrypt,
no ceremony. Whatever tool moves folders — a sync client,
`rsync`, a file manager, a USB stick — moves vaults.

## Syncing

This is the design centre. Put the vault folder inside a
synced folder (or sync services that support picking any
folder) and:

- The sync tool only ever handles ciphertext — it cannot
  leak what it never has.
- Because encryption is per-file, editing one document
  re-encrypts and re-uploads one small `.c9r` file, not a
  monolithic container. On a slow link this is the
  difference between seconds and an evening.
- Every machine with the password and any Cryptomator app
  (desktop, mobile, CLI) opens the same vault.

Discipline that keeps sync boring:

- **Lock before you leave.** Syncing an unlocked vault's
  `.c9r` files is safe; making *structural* changes from
  two machines at once — adding files on both while both
  are unlocked — is how conflicts are born.
- **Conflicts arrive as conflict copies.** If the sync
  tool reports a conflicted `.c9r`, don't delete in a
  panic: unlock, look at what you actually have through
  the cleartext view, and reconcile there. (Characterised
  from the design; the lab's single machine produced no
  conflicts to demonstrate.)
- **Never edit inside the vault folder by hand.** Renaming
  or "tidying" `.c9r` files corrupts the mapping between
  names and contents. The vault folder is a database that
  happens to be files; touch it only through an unlocked
  vault or a whole-folder copy.

## Sharing

Sharing a vault means sharing its password — there is one
password and it opens everything, with no per-person
accounts, no read-only mode and no revocation short of
changing the password and re-sharing the new one. For a
household folder or a small team's shared drive that is
often exactly right. For anything needing individual
access control, Cryptomator's own answer is its commercial
Hub offering, and other tools in the neighbours chapter
make different trade-offs. State it plainly to anyone you
share with: the password *is* the membership.

## Backing up

A locked vault backs up like any folder, and that backup
is already encrypted — a backup of a vault needs no
second layer of encryption to be safe on someone else's
storage. Two checks make restores boring instead of
memorable:

- The backup must include `masterkey.cryptomator` and
  `vault.cryptomator` — whole-folder copies do; a rule
  that "skips config files" would silently ruin you.
- Periodically unlock a *copy* of the backup (exactly the
  t08 manoeuvre) and read a file back, so the restore path
  is proven before it is needed.

**Reader check:** you should be able to copy a vault,
unlock the copy, and read a file from it — and explain why
per-file encryption makes sync cheap, why hand-editing the
vault folder is forbidden, and what a shared vault's
password really grants.
