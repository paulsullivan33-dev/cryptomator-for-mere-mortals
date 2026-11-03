# The WebDAV Door

**No mount, no problem: an unlocked vault served over**
**WebDAV behaves like a tiny file server** — and everything
done through it is done on ciphertext at rest.

This chapter is the lab's main path, and on a headless
server it could be yours too: scripts that must read or
write vault files without a desktop can serve the vault on
loopback and talk HTTP to it. On a desktop you would
normally use the mount instead and never see any of this.

## The shape of it

The fallback mounter runs an embedded WebDAV server bound
to `localhost` — in this lab on the assigned loopback port
18091 — under a fresh random path:

```text
http://localhost:18091/7d136c0f-9e31-45f3-b73f-29021f648818
```

The root answers a `PROPFIND` with an empty collection
until something is added. Everything below is plain
WebDAV, so the transcripts use `curl`; a file manager's
"connect to server" or any WebDAV client does the same
job.

## Adding files (transcript t05)

Two invented fixtures — a field note and a garden journal —
stand in for real files. WebDAV verbs do the work:

```text
MKCOL  /garden-journal            -> 201
PUT    /field-notes.txt           -> 201   (199 bytes)
PUT    /garden-journal/readme.txt -> 201   (137 bytes)
```

Listings through the same door show the world as you
expect it — cleartext names, real sizes:

```text
/                       field-notes.txt (199), garden-journal/
/garden-journal         readme.txt (137)
```

Those cleartext names exist only in the unlocked view.
Chapter 7 looks at the same moment from the vault folder's
side, where none of those names exist.

## Reading files back

A plain `GET` per file — and the only verdict that counts:

```text
$ sha256sum plain/field-notes.txt back/field-notes.txt
9652cd48...  plain/field-notes.txt
9652cd48...  back/field-notes.txt
```

Byte-for-byte identical, both files. Decryption is not
"close enough"; the hash either matches or the guide would
say so.

## Locking is stopping the process

There is no `lock` command. The unlock process *is* the
unlocked vault, so locking means ending it — the CLI
documentation names `SIGTERM` as the clean way, and the
lab's log shows the orderly result (transcript t07):

```text
Stopped ServerConnector ... {localhost:18091}
WebDavServer stopped.
```

After it, the port refuses connections (`curl` exit 7).
Nothing is left mounted, cached or half-open: kill the
process and the vault is locked, with only ciphertext on
disk — as it was before you started.

Two working rules for this door:

- **Loopback only, deliberately.** The server binds to
  localhost and the random path is a convenience, not a
  security boundary; anyone who can reach the port while
  the vault is unlocked can read the cleartext. Do not
  expose it beyond the machine.
- **The unlock process is a session.** Start it when you
  need the files, stop it when you don't — the same
  discipline as locking a mounted vault, made visible.

**Reader check:** you should be able to add a file through
the WebDAV door with `curl`, prove the round trip with a
hash, and lock the vault again with one signal — and you
should be able to say who else could read an unlocked
vault served this way.
