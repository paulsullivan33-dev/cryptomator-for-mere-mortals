# Proving the Encryption

**Claims are cheap; `grep` is not.** This chapter stands
outside the vault with nothing but the folder — exactly
what a storage provider, a thief with the synced copy, or
a curious co-worker would hold — and checks what it gives
away.

Everything here is transcript t06, run while the vault
held the two fixture files from Chapter 6.

## The tree an observer sees

```text
vault/d/7X/SLCHICG5CVCCVJW6EF7ZF2BXST2BSK/
    V9grweqazMopx9qsz1ioz1UYP-NxqsCrjOE=.c9r    (273 bytes)
    dirid.c9r                                    (172 bytes)
vault/d/X6/XEUD27YX56LLTPKIXDWLICISQARN46/
    25rsnx3QH3p68Ru1k3ly1FrOcbY4aEUJnE1LOsxqpQ==.c9r  (335)
    dirid.c9r                                    (88 bytes)
    v4dCc8wyPfU5j9kdicAscvS-KbhvfcJyu5nNA_fl.c9r/
        dir.c9r         <- the subfolder's link entry
vault/masterkey.cryptomator
vault/vault.cryptomator
vault/vault.cryptomator.9ADD23A3.bkup
```

Folders, files, organisation: all dissolved into bucket
names and ciphertext names. Nothing here says "field
notes" or "garden".

## The four greps

Each searched the **entire vault folder**, byte patterns
and all, and each came back empty (exit code 1 — no
matches):

```text
$ grep -r "field-notes"    vault/   -> no matches
$ grep -r "garden-journal" vault/   -> no matches
$ grep -r "QUIETRIVER-9274" vault/  -> no matches
$ grep -r "rain gauge"     vault/   -> no matches
```

- The **filename** is not in the vault.
- The **folder name** is not in the vault.
- A unique **marker sentence** planted inside the file is
  not in the vault.
- Ordinary **words from the text** are not in the vault.

And the ciphertext itself, looked at directly, is what
encryption is supposed to look like — `file` calls the
335-byte `.c9r` simply `data`, and its first bytes are
indistinguishable from random:

```text
000000 8e c4 39 19 31 6a a9 b9 94 45 7e 08 ...
```

## The tamper test (transcript t10)

Encryption that cannot detect meddling is only hiding.
So: copy the vault, flip exactly one byte at offset 100
of the note's ciphertext, unlock the copy, and ask for
both files.

```text
GET /field-notes.txt            -> 500
    log: AuthenticationFailedException:
         Authentication of chunk 0 failed.
GET /garden-journal/readme.txt  -> 200, sha256 identical
```

One flipped byte condemns exactly one chunk of exactly
one file. The neighbour is untouched because every file
carries its own key and its own authentication — the
practical meaning of "per-file encryption" from Chapter 2.

## What an observer *can* still learn

Honesty cuts both ways. Holding only the vault folder, an
observer can see:

- **That it is a Cryptomator vault**, and its format
  version — the two config files announce it.
- **How many files and folders** there are, from the
  bucket and `.c9r` counts.
- **Approximate sizes** — ciphertext runs about 136 bytes
  over cleartext for small files (the header and
  authentication overhead), so sizes leak within a small,
  known margin.
- **When files change**, from timestamps — which a sync
  provider sees anyway.

What they cannot get is names, contents, or the folder
structure — the bucket layout does not even reveal which
encrypted file sits in which folder beyond bucket counts.
If your threat is "the provider reads my documents",
Cryptomator answers it. If your threat includes hiding
that documents exist at all, that is a different tool's
promise, not this one's.

**Reader check:** you should be able to run the four
greps against any vault you own and interpret both
outcomes — silence means the encryption is doing its job;
a single match would mean something is deeply wrong — and
list the three things an observer still learns: existence,
counts, and approximate sizes.
