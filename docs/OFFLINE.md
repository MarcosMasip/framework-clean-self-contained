# Offline Usage

After the first online run (`./dynamia up`) all required Maven dependencies are cached locally in your `~/.m2/repository` (and optionally in a vendored repo via `./dynamia vendorize`).

## Steps
1. While online run:
   ```bash
   ./dynamia up
   ```
2. Disconnect the network.
3. Validate offline readiness:
   ```bash
   ./dynamia offline-check
   ```
4. Start the demo again:
   ```bash
   ./dynamia demo
   ```

## Vendor Mode (Portable Cache)
To store dependencies inside the repository (for air‑gapped transfer):

```bash
./dynamia vendorize
```

Then build using the vendored repository:

```bash
MAVEN_OPTS='-Dmaven.repo.local=.m2repo' ./dynamia build
```

Pros: Fully self-contained.  
Cons: Increases repository size.

## Troubleshooting
| Issue | Cause | Fix |
|-------|-------|-----|
| Offline check fails | Some artifacts not cached | Re‑run `./dynamia up` online |
| Demo won’t start offline | Build artifacts cleaned | Run `./dynamia build` (will work offline if deps cached) |
| Different JDK on CI | Local JDK not distributed | Install JDK 21 on CI or add forthcoming auto-download feature |
