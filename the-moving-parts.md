# The Moving Parts

**A vault is four things: a folder, a key file, a signed**
**config token, and a tree of numbered buckets.** Everything
else is presentation.

## The vault folder

A vault lives in an ordinary folder — the lab's is simply
called `vault`. Nothing about the folder itself is special,
which is the point: any tool that can copy or sync a folder
can copy or sync a vault.

## masterkey.cryptomator — the crown jewels

When a vault is created, Cryptomator generates a random
**masterkey** — the key that ultimately protects every file
key in the vault. It is never stored in the clear. It is
stored wrapped (encrypted) in `masterkey.cryptomator`,
which in the lab's vault contains, in plain JSON:

```text
{
  "version": 999,
  "scryptSalt": "...",
  "scryptCostParam": 32768,
  "scryptBlockSize": 8,
  "primaryMasterKey": "...",
  "hmacMasterKey": "...",
  "versionMac": "..."
}
```

Read it as: the two halves of the masterkey (one encrypts,
one authenticates), each wrapped with a key derived from
your password by **scrypt** — a deliberately expensive
password-stretching function (the cost numbers are right
there in the file) — plus a check value over the format
version. Lose this file and the password has nothing left
to unwrap. Chapter 8 changes a vault's password and shows
that only this file's wrapping changes; the masterkey, and
therefore every encrypted file, stays the same.

## vault.cryptomator — the signed config token

Beside it sits `vault.cryptomator`, a small signed token
(a JWT). Decode the lab's and it says, in full:

```text
header:  {"kid": "masterkeyfile:masterkey.cryptomator",
          "alg": "HS256", "typ": "JWT"}
claims:  {"format": 8,
          "cipherCombo": "SIV_CTRMAC",
          "shorteningThreshold": 220, "jti": "..."}
```

It pins three facts: this vault is **format 8**, its
cipher combination is **SIV_CTRMAC**, and filenames longer
than 220 characters get shortened (see below). The token is
signed with the masterkey itself, so a token and a
masterkey file from different vaults will not pair up — the
lab met this indirectly: a vault folder whose token exists
but whose contents were never initialised fails to unlock
with `ContentRootMissingException` (transcript t04a).

Unlocking also writes a backup copy next to it,
`vault.cryptomator.<8 hex digits>.bkup`.

## The d/ tree — buckets by directory ID

Your folder structure does not appear in the vault at all.
Instead, every folder inside the vault — including the
root — gets a random **directory ID**, and its contents
live in a bucket under `d/`, named by a hash of that ID
(SHA-1 in base32, which is why the names use only A–Z and
2–7), split after the first two characters:

```text
vault/d/X6/XEUD27YX56LLTPKIXDWLICISQARN46/   <- the root folder
vault/d/7X/SLCHICG5CVCCVJW6EF7ZF2BXST2BSK/   <- one subfolder
```

Each bucket holds:

- `dirid.c9r` — the bucket's own encrypted directory ID.
- One `<name>.c9r` file per encrypted file, where `<name>`
  is the encrypted filename.
- For each subfolder, a `<name>.c9r/` **directory** holding
  a `dir.c9r` that links to the subfolder's ID — and so to
  its bucket.

Filenames are encrypted with **AES-SIV**, which is
deterministic: the same name in the same folder always
encrypts to the same ciphertext name, which is what lets
the software find a file by name without decrypting
everything — while revealing nothing about the name itself
to anyone without the key. (A name that encrypts beyond
the 220-character threshold is stored in a shortened form
with the full name kept inside; documented behaviour, not
needed by the lab's short names.)

## Per-file encryption, chunk by chunk

Each file's contents are encrypted with a fresh random
file key, stored in the file's own header. The content is
processed in chunks, each chunk authenticated — Chapter 7's
tamper test flips a single byte and gets back
`AuthenticationFailedException: Authentication of chunk 0
failed`, while the neighbouring file still reads perfectly.
A 199-byte note becomes a 335-byte `.c9r` file, and a
137-byte readme becomes 273 bytes: the same 136 bytes of
header and authentication overhead on each, regardless of
size.

## The doors in

Software opens a vault through a **mounter**. The official
tools offer three: a FUSE mount (the vault appears as a
drive — the normal desktop experience), a WebDAV server
whose address a file manager or `curl` can talk to, and on
Linux a GIO variant of the WebDAV mount. Chapter 5 walks
through which doors this lab could open, and why.

**Reader check:** you should be able to sketch a vault from
memory — `masterkey.cryptomator` (wrapped key),
`vault.cryptomator` (signed format token), `d/` (buckets
by directory ID) — and say which file a password change
rewrites, and which file you must never lose.
