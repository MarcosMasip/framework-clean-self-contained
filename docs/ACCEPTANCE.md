# Acceptance Verification Checklist

1. Online First Run
   - [ ] `./dynamia up` completes (build + dependency warm + demo starts)
2. REST Endpoint
   - [ ] Visit http://localhost:8080/api/demo/contacts returns JSON with keys data, pageable, response
3. Scaffold
   - [ ] `./dynamia new-app SampleApp --group com.example --package com.example.sample` creates `apps/sample-app`
4. Offline Mode
   - [ ] Disconnect network
   - [ ] `./dynamia offline-check` passes
   - [ ] `./dynamia demo` starts again
5. Vendor Mode (Optional)
   - [ ] `./dynamia vendorize` creates `.m2repo` and subsequent build works with `MAVEN_OPTS='-Dmaven.repo.local=.m2repo'`
6. Tests
   - [ ] `mvn -pl examples/demo-app test` passes `ContactApiSmokeTest`
7. VS Code
   - [ ] Recommended extensions appear
   - [ ] Tasks list includes build and run demo

If all boxes are checked the repository is considered self-contained for baseline workflow.
