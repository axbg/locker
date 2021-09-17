# locker

An app meant to simplify encryption and management of regularly updated files... in some particular use-cases.

Locker can be run either as a [GUI](#1-using-the-gui) or as a [CLI](#2-using-the-cli) program.

#

### 1. Using the GUI

![App preview](./.images/0-preview.png)

If a configuration is used often, it can be saved as a *Preference*, by clicking on the *Save Preference* button after a
value for each field is provided.

![Preference preview](./.images/1-preferences.png)

Then, the preference can be applied by selecting it in the *Load preference* dropdown.

![Applied preference](./.images/2-applied-preferences.png)

If you would like to move your preferences to another PC or share them with somebody else, you can use the
**Export Preferences/Import Preferences** feature.

Your preferences will be encrypted with a password of choice and will be exported to a selected location.

The import process is similar: select the encrypted preferences file, provide the right password, and, optionally, an
extension that will be appended after each imported name.

The imported preferences will be added along with the existing ones, overwriting those that share the same name.

![Share preferences](./.images/3-share-preferences.png)

Additionally, if you feel like your password minting game is not that strong, you can rely on the embedded password
generator that will create a complex, hard-to-break password.

![Password generator](./.images/4-password-generator.png)

#

### 2. Using the CLI

The application can be integrated into other workflows through its command-line interface.

Before a file or a directory can be encrypted or decrypted, a preference has to be defined.

The preferences defined through the GUI are shared with the CLI and vice-versa.

To define a preference in CLI mode, the app should be run as:

```bash
# java -jar locker.jar preference_name source destination operation_mode 
#   the operation_mode can be encrypt or decrypt  
java -jar locker.jar cli-en /Users/axbg/Desktop/un /Users/axbg/Desktop/en encrypt
```

If executed using this command, the new preference will be created and associated with a strong, randomly generated
password.

The application does not allow users to specify a custom password through its CLI, but passwords of other existing
preferences can be reused, to allow the creation of an encryption-decryption preferences pair.

```bash
# java -jar locker.jar prefernece_name source destination operation_name pair_preference_name
java -jar cli-dec /Users/axbg/Desktop/en /Users/axbg/Desktop/dec decrypt cli-en
```

After a preference is created, it can be executed by passing its name as the only input parameter.

```bash
# Run the encryption preference defined previously
java -jar locker.jar cli-en

# Run the decryption preference defined previously
java -jar locker.jar cli-dec
```

To list all the preferences registered in the application, run:

```bash
java -jar locker.jar list
```

Using the following output, additional information about a particular preference can be requested:

```bash
# java -jar locker.jar list preference-name
java -jar locker.jar list cli-en
```

When a preference has to be removed, the following command can be used:

```bash
# java -jar locker.jar remove preference-name
java -jar lcoker.jar remove cli-en
```

#

You can clone the repo and build the app, or directly download the provided [jar](./locker.jar).

**shelf.jar SHA-512 checksum and VirusTotal scan report:**
[B6DF6B3E7760F30492491A180D37A50EF317A31C4F35E191CF57EF10F9481A52](https://www.virustotal.com/gui/file/B6DF6B3E7760F30492491A180D37A50EF317A31C4F35E191CF57EF10F9481A52/detect)