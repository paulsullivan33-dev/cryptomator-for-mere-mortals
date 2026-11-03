# Passwords and Recovery

**The password is not a login. It is the key** — everything
in this chapter follows from that one fact.

## A wrong password (transcript t07)

```text
$ cryptomator-cli unlock --password:env=VAULT_PW ...
org.cryptomator.cryptolib.api.InvalidPassphraseException
exit code: 1
```

That single line is the whole event. The scrypt-derived
key fails to unwrap the masterkey and the check value
refuses it. No partial access, no hint about how close
you were, no lockout counter to wait out — and no damage:
type the right password and the vault opens as if nothing
happened. Brute force fares no better than guessing: the
scrypt cost in the masterkey file (cost parameter 32768)
makes each attempt deliberately expensive, which is why a
strong, unique password plus a password manager is the
whole defence, and a sufficient one.

## Changing the password (transcript t11)

Changing a password re-wraps the *same* masterkey under a
new password — the encrypted files are not touched, which
is why a password change on a huge vault is instant.

The desktop app does this from the vault's options. The
lab did it with the project's library
(`MasterkeyFileAccess.changePassphrase`, via a small
helper) on a *copy* of the vault, then let the official
CLI judge the result both ways:

```text
unlock vault-pwchange with the OLD password
  -> InvalidPassphraseException
unlock vault-pwchange with the NEW password
  -> Unlocked and mounted vault successfully
GET field-notes.txt -> sha256 identical to the original
```

Old refused, new accepted, contents untouched: that is a
password change, proven rather than asserted.

## The recovery key

When the desktop app creates a vault, it offers a
**recovery key** — a long string that can reset a
forgotten password. It works because it can unwrap the
masterkey through a second wrapping, independent of the
password. Treat it exactly like the password: store it in
the password manager (or print it and store the paper
somewhere genuinely safe), because anyone holding it holds
the vault. The lab did not run the recovery flow — the CLI
has no surface for it and the desktop app needs a display
— so this paragraph is documented-only, from the project's
own documentation.

## The lost-password truth

Without the password *and* without the recovery key, the
vault is unrecoverable. Not "hard": unrecoverable. The
masterkey is random, the wrapping is scrypt plus strong
encryption, and there is no escrow, backdoor or support
desk override — the support desk will tell you the same.
This is the property you are buying when you choose
client-side encryption; the price is that password
hygiene stops being advice and becomes load-bearing:

- Store the vault password in a password manager.
- Save the recovery key when the app offers it.
- Back up `masterkey.cryptomator` *with* the vault (it is
  inside the vault folder, so any whole-folder backup
  already has it — but a backup that excludes it is a
  backup of nothing).
- Never put a real vault password on a command line, in a
  script, or in a shell command you paste into a chat —
  command lines are logged in shell history and process
  lists. The lab's transcripts pass the throwaway password
  by environment variable for this reason, and warn about
  it in the CLI's own README besides.

**Reader check:** you should be able to say why a password
change is instant even on a huge vault, what the recovery
key really is (a second wrapping of the same masterkey),
and the exact set of things that can still open a vault
whose password is forgotten: the recovery key, or nothing.
