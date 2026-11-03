# The Neighbours

**Cryptomator is one shape of answer: per-file encryption**
**of a synced folder. The neighbours solve nearby problems**
**with different shapes.** (Characterised from their
designs and documentation, not re-run for this guide —
except where a sibling guide in this series did the
running.)

## VeraCrypt — one container, whole volume

A VeraCrypt volume is a single encrypted container file
that mounts as a drive. Inside it, a whole filesystem;
outside it, one opaque blob. Choose it when you want an
encrypted *disk* — plausible deniability options, system
volume support, no per-file structure visible at all. The
trade against Cryptomator: the blob is one file, so sync
and incremental backup see a mountain move when a molehill
changes, and two machines mounting it concurrently is not
a thing. The series' VeraCrypt guide covers it hands-on.

## LUKS — the disk itself

Linux's LUKS encrypts a whole partition or disk, opened at
boot or mount. It answers "the laptop was stolen while
off" completely and "the cloud provider can read my
folder" not at all — once mounted, everything above it is
cleartext, and it does not travel through a sync folder.
Different layer, different threat; often used *under* a
vault rather than instead of one.

## age — files, one at a time

age encrypts individual files, usually for transfer: one
file in, one encrypted file out, typically to someone's
public key. No live folder, no sync story — and no
password-only mode as its centre of gravity. For "send
this spreadsheet safely" age is the sharper tool; for "my
documents folder, continuously" it is not a tool at all.
The series covers age in its own guide.

## gocryptfs — the same family, a different house

The closest neighbour: a FUSE overlay that encrypts files
individually into a backing folder, Linux-centred, with a
config file instead of a masterkey file, and a reverse
mode for encrypted backup views. If Cryptomator did not
exist, this is what many Linux users would build the same
workflow on. Cryptomator's edges are its cross-platform
desktop and mobile apps and the format's single,
app-driven design; gocryptfs's edges are its scriptability
and reverse mode. Same promises, same caveats — including
"metadata (counts, sizes) still shows".

## rclone crypt — encryption at the sync layer

rclone's `crypt` wraps a cloud remote so uploads are
encrypted in transit to storage you never keep locally.
Choose it when there is no local folder to protect — a
server pushing backups straight to object storage. Choose
Cryptomator when the folder also lives on your machines
and people open it with apps. They compose fine and solve
adjacent thirds of the problem; the series' rclone guide
covers crypt's setup.

## Archives and the ancestor

A password-protected 7-Zip/zip archive is encryption as a
snapshot: fine for sending or shelving one bundle, with no
live view and all-or-nothing edits. And a word for EncFS,
the design Cryptomator's generation learned from: it
pioneered the encrypted-folder idea, and a published
security audit found weaknesses in its design that its
maintainers could only partly address — the reason modern
advice reaches for the tools above instead.

## Choosing, in one breath

Synced folder on storage you don't control → Cryptomator
(or gocryptfs on Linux). Whole disk or deniable volume →
VeraCrypt or LUKS, by platform. One file to one person →
age. No local copy, straight to cloud → rclone crypt. One
bundle to shelve → an encrypted archive.

**Reader check:** given a scenario — stolen laptop, nosy
sync provider, one file to an accountant, a backup already
in the cloud — you should be able to pick the neighbour
whose shape matches, and say what Cryptomator would do
worse there.
