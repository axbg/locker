# locker

An app meant to simplify encryption and management of regularly updated files... in some particular use-cases.

![App preview](./.images/0-preview.png)

If a configuration is used often, it can be saved as a *Preference*, by clicking on the *Save Preference* button after a
value for each field is provided.

![Preference preview](./.images/1-preferences.png)

Then, the preference can be applied afterwards, by selecting it in the *Load preference* dropdown.

![Applied preference](./.images/2-applied-preferences.png)

If you would like to move your preferences to another PC or share it with somebody else, you can use the
**Export Preferences/Import Preferences** feature.

Your preferences will be encrypted with a password of choice and will be exported to a selected location.

The import process is similar: select the encrypted preferences file and provide the right password, and they will be
added along with your existing preferences.

![Share preferences](./.images/3-share-preferences.png)

Additionally, if you feel like your password minting skills can be improved, you can rely on the embedded password
generator that will create a complex, hard-to-break password.

![Password generator](./.images/4-password-generator.png)

#

You can clone the repo and build the app, or directly download the provided [jar](./locker.jar).

**shelf.jar SHA-512 checksum and VirusTotal scan report:**
[E917E09B2DF1DE137810CEFC172F63A79BB1509B5CBCBD39257AF97A90102D6D](https://www.virustotal.com/gui/file/E917E09B2DF1DE137810CEFC172F63A79BB1509B5CBCBD39257AF97A90102D6D/detect)