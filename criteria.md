# Writing Criteria for This Guide

Every guide in this series is written against the same nine
points. They are restated here so readers can hold the guide
to them, and so the next revision knows what it must keep
true.

1. **Practically enabling, ethically bounded.** The reader
   finishes able to create, unlock, fill, sync and back up a
   real vault. Cryptomator is for encrypting **your own**
   files on storage you do not fully control; that is the
   use this guide teaches, and the privacy line in point 8
   is stated before any power is handed over.

2. **No-filler outcome.** Every chapter ends with a reader
   check the reader can pass, and the guide as a whole ends
   with a working vault, a proven round trip, grep evidence
   for the encryption claims, and a tested backup manoeuvre
   (unlocking a copy) — artifacts, not impressions.

3. **Anti-hype.** What Cryptomator cannot do gets equal
   weight with what it can: the limits chapter is a chapter,
   not a footnote, and Chapter 7 lists what an observer
   still learns from a vault folder. No claim in the guide
   rests on the word "military-grade".

4. **Teaches the standard, not a shortcut.** The paths
   taught are the project's own: the desktop app for
   creating and mounting, the official CLI for unlocking,
   the published vault format for the model. The lab's
   library detour is labelled a detour every time it
   appears, with the supported path named beside it.

5. **Accessible.** Key terms are defined where they first
   appear and collected in the Glossary; no chapter assumes
   cryptography beyond what the moving-parts chapter built
   up; the worked files are small and invented.

6. **Reproducible.** Versions are pinned (CLI 0.6.2,
   desktop 1.19.6), downloads are checksum-verified, and
   every load-bearing claim traces to a numbered transcript
   in `cryptomator-lab/`. The fixtures, and the two helper
   programs the lab needed, are in the lab folder too.

7. **Cryptomator's real limits, plainly.** The CLI cannot
   create vaults — verified live — and has no lock, share
   or password command. Mounting needs FUSE, which some
   environments flatly do not have. Metadata (counts,
   approximate sizes, change times) is not protected. A
   vault has exactly one password and no access control.
   An unlocked vault is cleartext to anything running as
   you. Very long filenames are shortened by design
   (threshold 220), which the format documents and this
   lab's short names never triggered.

8. **The privacy line.** A forgotten vault password with no
   recovery key means lost data — permanently, by design —
   and this guide says so on its home page, not just in the
   fine print. The vault folder is safe to sync and back up
   *because* it is ciphertext; conversely, a real password
   never goes on a command line, into a script or into a
   transcript (the lab's throwaway password travels by
   environment variable, and is labelled throwaway). The
   `masterkey.cryptomator` file is named as what it is: the
   crown jewels, backed up with the vault and guarded like
   it.

9. **Series fit.** This guide mirrors the shared
   root-level book layout of the series, carries its footer,
   links the index, and treats its neighbours fairly —
   VeraCrypt, LUKS, age, gocryptfs and rclone crypt are
   characterised by their actual shapes, with the cases
   where each of them beats Cryptomator said out loud.

## Verification notes for this guide

Everything load-bearing in the main path was **run live**
in the lab (cryptomator-cli 0.6.2 on its bundled runtime;
the project's cryptolib 2.2.0 / cryptofs 2.8.0 libraries on
OpenJDK 21 for vault creation and the password change —
the same libraries the official CLI ships). Concretely:

- Vault creation via the libraries: masterkey file, signed
  format-8 token, initialised `d/` root (`t03`), including
  two honest failures on the way (`t03a`, `t03b`) and the
  incomplete-vault failure a hand-shortcut produces
  (`t04a`).
- Unlock by the official CLI through the WebDAV fallback
  mounter on loopback port 18091; files added by `MKCOL`/
  `PUT`, listed by `PROPFIND`, read back by `GET`, sha256
  identical (`t04`, `t05`).
- Encryption evidence from outside the vault: full tree,
  ciphertext sizes and overhead, and four `grep` searches
  (filename, folder name, marker sentence, plain words)
  returning no matches (`t06`).
- Lock by SIGTERM with the server stopped cleanly; wrong
  password refused verbatim; non-vault folder refused
  verbatim (`t07`).
- Portability: a whole-folder copy unlocked independently
  and returned a byte-identical file (`t08`).
- Integrity: one flipped ciphertext byte produced an
  authentication failure for that file only (`t10`).
- Password change via the libraries, proven by the CLI
  both ways — old refused, new accepted, contents
  untouched (`t11`).
- Desktop package evidence: AppImage checksum, extraction,
  startup logging, and its two headless failures (`t09`);
  FUSE unlock failure captured in full (`t04`).

**Documented-only** — described from the project's
documentation and the distributions' own behaviour, never
presented as run: creating and using vaults in the desktop
GUI (vault creation dialog, mounted-drive use, vault
options); the recovery key flow; the GIO mounter; mobile
apps; Cryptomator Hub and any licensing features; and the
neighbour tools in Chapter 11, which no part of this lab
installed. The desktop AppImage's *failure* to run headless
is run evidence; its success on a real desktop is not
claimed.
