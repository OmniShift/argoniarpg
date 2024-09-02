# Argonia RPG

## Setup guide

If you've never ran a Java application before:
1) It is easiest to download an IDE (a program in which you write code) like the free [IntelliJ Community Edition](https://www.jetbrains.com/idea/download/?section=windows). Download and install it.
2) From the GitHub website, click the green `Code` button and copy the URL (it should end in `.git`)
3) In IntelliJ, go to `File -> New -> Project from Version Control...` and paste the URL you copied. Click `Clone` and the IDE will download all source code into the program.
4) You should get a popup in the lower-right saying `Maven 'argoniarpg' build scripts found'`. Click the "Load Maven Project" to have the IDE automatically detect where the application should 'start'.

After you've got the project loaded in the IDE:
1) Download and setup a [local MySQL8 database](https://dev.mysql.com/downloads/installer/). Save the username and password you register during installation in for example a password manager and remember what you named your first database. You'll need these settings to run the application.
2) Create an `application-local.yml` file to the root folder, which will function to overwrite the empty database settings in [application.yml](\src\main\resources\application.yml) as this is sensitive information (these are the `url`, `username`, and `password` fields under `spring.datasource`). For that reason, **<ins>never push that file to the GitHub repo</ins>**. The url will have to include the scheme and database name (e.g. `jdbc:mysql://localhost:3306/argonia_rpg`).
3) Run the application through [ArgoniarpgApplication](\src\main\java\com\kingargaroth\argoniarpg\ArgoniarpgApplication.java) with the `local` profile.
   1) In IntelliJ on the first time, you will have to 'select' the application by going to `src\main\java\com\kingargaroth\argoniarpg` in the project tree to the left, then right-click ArgoniarpgApplication, and select `Run ArgoniarpgAppl....main()`. This will fail, as it doesn't use the local profile, but it will show "ArgoniarpgApplication" in the top-right dropdown.
   2) Now click the dropdown in the top right and selecting `Edit Configurations...`. Click the `Modify options` link and enable `Add VM options`. In the field that appears enter: `-Dspring.profiles.active=local`. Then click Ok. To run the application, click the green play-button in the top-right next to the "ArgoniarpgApplication" dropdown. 
4) The first time you run the application, there will be a popup in the lower-right asking you to enable Lombok annotations. Do this.
5) If the application runs successfully (the last line in the terminal should read `Started ArgoniarpgApplication in ... seconds (process running for ...)`), go in your browser to `http://localhost:8080/` and you should see the application.

### Notes
* The character demo saves the generated characters to the database. After reloading, you cannot load the character again. You can only find it again in the database with for example the MySQL Workbench program you've probably downloaded.
* Right now, when creating a character, it will always say "OmniShift89" as user as this is hardcoded. You can change this in [index.js](src\main\resources\static\index.js) on line 19 and 41. 
* The combat demo does not reset after combat. This means dead combatants will cause weird things to happen in later combats. Just reload the page to reset.
* The combat demo does nothing with the created characters from the character demo. The stats of the enemy and all combatants are hardcoded. If you want to change them, you can do that in the `enemy` variable on line 2 and the `combatant` variable on line 15 of [combat.js](src\main\resources\static\combat.js).