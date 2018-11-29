# Jenkins scripts

Jenkins scripts to automize kenkins jobs creation

*Note*: you can create jenkins jobs manually but this will facilitate job creation

## Setup

First you need to set two environment variables. Go to your jenkins server, `/me/configure` route and get the informations in the `API Token` fields:

```bash
export API_USER="<USER_ID>"
export API_TOKEN="<API_TOKEN>"
```

Then you can use the script `create-job-from-template` to create a new job.

## Create a job from a template

- Create a new config file from a `<job>-config.py`:
    - `cp <job>-config.py <job>-my-config.py`
- Enter your configuration in the new file `<job>-my-config.py`
- Run the script:
    - `./create-job-from-template job-name <job>-template.xml <job>-my-config.sh`

## Templates

### PR Bitbucket validation

This job will be able to validate PR of a bitbucket repository. For each PR, it will get the branch, try to merge it on the destination branch, and run a script. The script should generate errors if the PR is not valid (failed tests, merge conflicts, bad code style ...)

Name: `job-validate-bitbucket-pr`

Template: `job-validate-bitbucket-pr-template.xml`
Config: `job-validate-bitbucket-pr-config.py`

Example:
```
./create-job-from-template saretec-devfactory-PR job-validate-bitbucket-pr-template.xml job-validate-bitbucket-pr-config.py
```

### Continuous deployment with Nestincloud

This job will watch a branch on the git repository. For each new commit, it will trigger a build and deploy the new version to nestincloud. See the config for configuration options.

Name: `job-deploy`

Template: `job-deploy-template.xml`
Config: `job-deploy-config.py`

Example:
```
./create-job-from-template saretec-devfactory-DEV job-deploy-template.xml job-deploy-config.py
```
