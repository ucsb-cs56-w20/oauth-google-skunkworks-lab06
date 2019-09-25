

A simple Spring Boot webapp using:
* Thymeleaf templating
* Pulling in Bootstrap from CDN.
* Github OAuth

I started with the [code in the git repo](https://github.com/spring-guides/gs-serving-web-content.git) in the [Spring Guide: Serving Web Content](https://spring.io/guides/gs/serving-web-content/>.

I then added in Bootstrap, loosely following instructions from two different tutorials:
* [Ömer Yazir's tutorial on integrating Spring Boot with Bootstrap and Thymeleaf](https://medium.com/@omeryazir/how-to-integrate-spring-boot-with-bootstrap-and-thymeleaf-5744fc8475d)
* [Bootstrap's own getting started guide](https://getbootstrap.com/docs/4.3/getting-started/introduction/) which has the example code for getting the CSS and JavaScript files from a CDN

Finally, I added Github OAuth; you can see the pull request for the
very small code change that did this.

| Type this | to get this result |
|-----------|------------|
| `mvn package` | to make a jar file|
| `mvn spring-boot:run` | to run the web app|
| in browser: `http://localhost:8080/greeting/` | to see "Hello, World!" |
| in browser: `http://localhost:8080/greeting?name=Gauchos` | to see "Hello, Gauchos!"
