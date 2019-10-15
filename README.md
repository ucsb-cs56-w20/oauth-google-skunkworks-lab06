A simple Spring Boot webapp using:
* Thymeleaf templating
* Pulling in Bootstrap from CDN.
* Github OAuth

I started with the [code in the git repo](https://github.com/spring-guides/gs-serving-web-content.git) in the [Spring Guide: Serving Web Content](https://spring.io/guides/gs/serving-web-content/).

I then added in Bootstrap, loosely following instructions from two different tutorials:
* [Ömer Yazir's tutorial on integrating Spring Boot with Bootstrap and Thymeleaf](https://medium.com/@omeryazir/how-to-integrate-spring-boot-with-bootstrap-and-thymeleaf-5744fc8475d)
* [Bootstrap's own getting started guide](https://getbootstrap.com/docs/4.3/getting-started/introduction/) which has the example code for getting the CSS and JavaScript files from a CDN

Finally, I added Github OAuth; you can see the pull request for the
very small code change that did this.  For this I followed this tutorial:
<https://youtu.be/W0UqG0iUpYk>

This also involves handling Secrets; for that I used the technique
described here: <https://github.com/ucsb-cs56-pconrad/spring-boot-app-config>

| Type this | to get this result |
|-----------|------------|
| `mvn package` | to make a jar file|
| `mvn spring-boot:run` | to run the web app|
| in browser: `http://localhost:8080/greeting/` | to see "Hello, World!" |
| in browser: `http://localhost:8080/greeting?name=Gauchos` | to see "Hello, Gauchos!"
| `./checkLocalhost.py` | to check the syntax of your `localhost.json` file |
| `./setHerokuEnv.py` --app APPNAME` | to check the syntax of your `heroku.json` file  and set the configuration variables for Heroku app `APPNAME` (requires logging in to Heroku CLI first)|

# To Configure Github OAuth

You first need to generate a Client ID and Client Secret.

First, visit the Settings page of either your user account or an
organization account, and click "Developer Settings", then "OAuth
Apps", then "Create New OAuth App".

Fill in:
* Application Name: Anything you want, but I suggest the name of the repo followed by "on localhost",<br>
   e.g. "spring-boot-thymeleaf-bootstrap-oauth on localhost"
* Homepage URL: Must be `http://localhost:8080`
* Application Description is optional, but if entered will be shown to users the first time they authorize Github OAuth
* Authorization Callback URL: Must be exactly: `http://localhost:8080/login/oauth2/code/github`

Then click "Register Application"

Note that the Client ID and Client Secret will need to be different for running on localhost, vs. running on, for example, Heroku.   If you want to run in both places, you'll need two sets of client-id/client-secret.

Finally, the client secret SHOULD NEVER BE COMMITTED TO GITHUB.  That means, typically, that while it is ok to to put the client id in the `application.properties` file, the client secret should NOT be put there.  
  * If you accidentally do this, REVOKE IT IMMEDIATELY, and generate a new one.  It is not sufficient to just do a new commit that deletes it from the code, because its already there in the history.  
  
Instead, we typically define the client secret via an Environment variable before we run the command to start up the Spring Boot application.

# Maven Dependencies for Github OAuth

You will need to add the following dependencies to your `pom.xml`.
* `org.springframework.boot:spring-boot-starter-security`
* `org.springframework.boot:spring-boot-starter-jose`

For testing, the following may also be helpful:

* [`org.springframework.security:spring-security-test`](https://mvnrepository.com/artifact/org.springframework.security/spring-security-test>

# Defining the client id and client secret

## Using the files on localhost

1. `cp localhost.json.SAMPLE localhost.json`

2. Edit `localhost.json` with the values that you need.

2. Run the Python script `checkLocalhost.py` to make sure your JSON syntax is correct. It's a lot
   easy to check that with the script than to just get no results from your Spring Boot app because
   the JSON is getting ignored.

3. Execute `source env.sh` OR `. env.sh`

   Those both do the same thing.  NOT the same as `./env.sh`

   The space between `.` and `env.sh` is on purpose.

   This defines an environment variable `SPRING_APPLICATION_JSON` which
   has property values that override the ones in your `src/main/resources/applicaiton.properties` or `src/main/resources/application.yml` with new values.

   Those are the values you use when running on localhost.

   The file `localhost.json.SAMPLE` should contain "fake data" for all the
   values you need, and the README.md should explain how to set up
   real values (e.g. how to set up github oauth, or firebase credentials,
   or google oauth, or an mlab database, etc. etc.)

## Using the files to run on heroku

1. Copy from `heroku.json.SAMPLE` to `heroku.json`

2. Edit the file to set the values for heroku

3. Run `./setHerokuEnv.py --app name-of-your-herokuapp`

   If you get a permission error, do this first:

   ```
   chmod u+x setHerokuEnv.py`
   ```

   This sets the config variables on Heroku to the values in your
   `heroku.json`.   You can see those value in the Heroku dashboard config
   vars.

   