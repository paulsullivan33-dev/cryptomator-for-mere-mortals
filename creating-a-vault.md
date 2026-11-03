# Creating a Vault

**On your machine: open the desktop app, choose a folder,**
**set a password, done. In this lab: an honest detour,**
**because the CLI cannot create vaults — proven, not**
**rumoured.**

## The finding

The natural first command does not exist. Transcript t02,
verbatim:

```text
$ cryptomator-cli create vault --password:env=VAULT_PW
Unmatched arguments from index 0: 'create', 'vault'
Did you mean: cryptomator-cli list-mounters?
exit code: 2
```

The CLI's own README agrees: it "unlocks a single vault of
vault format 8". Current and older releases alike are
unlock-only tools. Any tutorial that creates a vault with
`cryptomator-cli` is describing a tool that isn't there.

So on a real machine you create vaults in the desktop app:
pick a name, pick where the vault folder lives (inside the
folder your sync tool watches), choose a strong password,
and optionally save the **recovery key** it offers you —
Chapter 8 explains why that key matters. The app writes
the three things Chapter 2 described: the wrapped
masterkey file, the signed config token, and the first
`d/` bucket.

## The lab's detour, labelled as one

A lab that cannot create a vault cannot test one — so the
lab created its vault with the project's own released
libraries, the exact `cryptolib` and `cryptofs` code the
CLI itself carries, driven by a small Java helper
(`CreateVault.java`, in the lab folder). Two steps, both
visible in transcript t03:

1. **Generate and wrap a masterkey.**
   `Masterkey.generate()` makes the random masterkey;
   `MasterkeyFileAccess` wraps it under the lab password
   and writes `masterkey.cryptomator`. (The helper passes
   an empty "pepper" byte array — confirmed as what the CLI
   itself uses, by inspecting the CLI's own bytecode.)
2. **Initialise the filesystem.**
   `CryptoFileSystemProvider.initialize()` writes the
   signed `vault.cryptomator` token — format 8, SIV_CTRMAC,
   shortening threshold 220 — and creates the encrypted
   root bucket under `d/`.

The result is a completely standard vault: the official
CLI unlocks it, the official libraries read it, and
nothing about it is lab-specific. The lab password is a
throwaway used only in the transcripts
(`purple-lantern-42`); yours should come from a password
manager and live nowhere else.

Two failures on the way are kept in the transcripts
because they teach the shape of the thing:

- **t03a** — a config built without a cipher combination
  dies with a `NullPointerException` while signing the
  token. The token really does pin the cipher choice, so
  the builder insists you make one.
- **t03b** — put the CLI's own app jar on the helper's
  classpath and Java 21 refuses to load it:
  `UnsupportedClassVersionError` (its config classes are
  compiled for Java 24, the CLI's bundled runtime — the
  libraries are fine on 21). A reminder that the CLI is a
  sealed appliance, not a development kit.

And one from the next chapter's doorstep, **t04a**: a
folder with a masterkey and a token but *no* initialised
`d/` tree is not yet a vault — unlocking it fails with
`ContentRootMissingException`. Creation is not complete
until the encrypted root exists.

## What you should take away

- Create vaults in the desktop app. That is the supported
  path and the only one the project's tools offer end to
  end.
- A vault's birth certificate is two small files —
  `masterkey.cryptomator` and `vault.cryptomator` — plus
  the first bucket in `d/`.
- The format is library-defined, not app-defined: the same
  vault opened by the CLI in this lab opens in the desktop
  app and the mobile apps, because all of them speak
  format 8 through these libraries.

**Reader check:** you should be able to explain why this
guide's vault was built by a helper program, name the two
files and one directory a creation must produce, and say
where a normal reader creates vaults instead.
