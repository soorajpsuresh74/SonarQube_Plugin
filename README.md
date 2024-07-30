# SonarQube_Plugin
Adding features to open-source SonarQube


## Overview

The SonarQube Custom Plugin adds a set of custom rules to enhance static code analysis in SonarQube. This plugin introduces a new repository called `HardCoded Secrets` designed to identify specific patterns in code that are deemed problematic.

## Features

- **HardCoded Secrets**: Rules that checks for specific coding patterns and reports violations currently concentrating the hardcoded values.
- **Compatibility**: Works with SonarQube versions [working]

## Installation

1. **Download the Plugin**

   Download the latest version of the plugin JAR file or execute the maven file.

2. **Install the Plugin**

   - **SonarQube Server**: Go to the SonarQube administration page.
   - **Upload Plugin**: Navigate to `Administration` -> `System` -> `Update Center` -> `Installed Plugins`.
   - **Restart SonarQube**: After uploading, restart the SonarQube server to apply the changes.

3. **Verify Installation**

   After restarting, check the list of installed plugins to ensure that the Custom Plugin is active.

## Configuration

1. **Enable the Custom Rule**

   Navigate to `Quality Profiles` in the SonarQube dashboard:
   - Select a quality profile.
   - Add the `Custom Rule` to the profile.

2. **Configure Rule Parameters**

   If the rule requires any configuration, navigate to `Rules` and adjust the settings as needed. [working]

## Usage

The `Custom Rules` will automatically run during code analysis. It will check for specific patterns as defined and report violations according to the rule's configuration.

### Example

Here’s a brief example of what the rule checks:

```python

def foo_function():
  secret = "my_hardcoded_secrets"
  pass

