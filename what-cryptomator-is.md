# What Cryptomator Is

**The problem: cloud storage can read your files.**
**Cryptomator's answer: encrypt each file before it**
**leaves your machine, and keep the key yourself.**

Put a document in a typical sync folder and it travels, and
rests, in a form the storage provider can read. Providers
promise to be careful, and mostly are — but "careful" is a
policy, not a property of the file. Policies change,
accounts get compromised, and a subpoena does not care how
careful anyone was.

Cryptomator changes the property of the file. You work with
a normal folder of normal files — that is the *unlocked
vault*. Underneath, every file is encrypted individually,
on your machine, before it is written to the folder that
actually syncs — the *vault folder*. The sync service only
ever stores and moves ciphertext.

## The shape of the answer

- **Per-file encryption.** Each file gets its own random
  key. One file changing means one encrypted file changes —
  which is exactly why Cryptomator suits sync folders where
  whole-disk or whole-container tools struggle.
- **Filename encryption too.** Names are encrypted with the
  contents, so the vault tree shows neither what a file is
  called nor what is in it. The lab proves this with `grep`
  in Chapter 7.
- **A vault is just a folder.** No special partition, no
  container image, no service. Copy it, back it up, put it
  on a stick: it is files all the way down. Chapter 9 copies
  one and opens the copy.
- **One password, no backdoor.** The password unwraps a
  randomly generated masterkey. There is no recovery by the
  project, by design.

## Where it fits

Reach for Cryptomator when the storage is not fully yours —
a consumer cloud drive, a synced share, an off-site backup
target — and the files are yours to protect. It is at its
best in a folder that syncs automatically, because the
encryption happens before sync, continuously, with no
separate "encrypt now" step to forget.

It is the wrong tool for encrypting a whole stolen laptop
(that is full-disk encryption's job — see the neighbours
chapter), for encrypting one file to email someone (a
single-file tool is simpler), and for sharing with people
who each need their own access (a vault has one password;
everyone who has it has everything).

One honest limit, up front: Cryptomator hides contents and
names. It does not hide that the vault exists, roughly how
much is in it, or how many files there are. Chapter 7 shows
exactly what an observer holding the vault folder can and
cannot learn.

**Reader check:** you should be able to say why encrypting
files one at a time, rather than in one big container, is
what makes a vault sync-friendly — and why a forgotten
password is unrecoverable by design, not by oversight.
