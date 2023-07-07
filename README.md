# Shorty URL Shortener
This a URL Shortener app which you can easily add to your project with little bit of configuration.  
Here are some functionality of the app  
1. This app is created in Java Springboot.
2. This app takes long url and return short url which you can easily fire on web and go to the long url.
3. It uses role based authentication.
4. For authentication it uses email and password which you have used while signing up.
5. JWT Token is used for authentication.
6. One cool thing  --> you can customize the length of short url key length.

Here is the sample authentication api call diagram !!

![Auth api call diagram](/src/main/resources/static/img/Sample%20Api%20call%20diagram.drawio.png)  

## Sample Api Calls
1. Sign up request [POST]

<table>
<tr>
<td> Status </td> <td> Method </td> <td> URL </td> <td> Request </td> <td> Header </td> <td> Response </td>
</tr>
<tr>
<td> 200 </td>
<td>POST</td>
<td>

```bash
http://localhost:8080/auth/signup
```
</td>
<td>

```json
{
    "name":"John Doe",  
    "email":"john@gmail.com",
    "password":"john@1234"
}
```
</td>
<td>
 NO User added header
</td>
<td>

```json
{
    "id":1,
    "name":"John Doe",  
    "email":"john@gmail.com",
    "password":"$2a$10$uhUIUmVWVnrBWx9rrDWhS."
}
```
</td>
</tr>
<tr>
<td> 200 </td>
<td>POST</td>
<td>

```bash
http://localhost:8080/auth/login
```
</td>
<td>

```json
{
    "email":"john@gmail.com",
    "password":"john@1234"
}
```
</td>
<td>
 NO User added header
</td>
<td>

```json
{
    "token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
            eyJlbWFpbCI6ImpvaG5AZ21haWwuY29tIiwic
            GFzc3dvcmQiOiJqb2huQDEyMzQifQ.ibVqO0r
            g6jwJ3uIEvZAeH_Ecg7T2bDW2lXeRJr2stW4",
    "username":"john@gmail.com"
}
```
</td>
</tr>
<tr>
<td> 401 </td>
<td>POST</td>
<td>

```bash
http://localhost:8080/auth/login
```
</td>
<td>

```json
{
    "email":"john@gmail.com",
    "password":"john@12"
}
```
</td>
<td>
 NO User added header
</td>
<td>

```
Bad Credentials !!
```
</td>
</tr>
<tr>
<td> 200 </td>
<td>POST</td>
<td>

```bash
http://localhost:8080/home/saveurl
```
</td>
<td>

```json
{
    "originalUrl":"www.google.com"
}
```
</td>
<td>

```
Authorization : Bearer <JWT Token>
```
</td>
<td>

```json
{
    "id":1,
    "originalUrl":"https://www.google.com",
    "shortUrl":"24WnSpXt",
    "creationDate":"2023-07-07T17:09:42.411",
    "expirationDate":" 2023-11-04T17:09:42.411"
}
```
</td>
</tr>
<tr>
<td> 200 </td>
<td>POST</td>
<td>

```bash
http://localhost:8080/home/get-user
```
</td>
<td>

```bash
No body
```
</td>
<td>

```
Authorization : Bearer <JWT Token>
```
</td>
<td>

```json
{
    "id":1,
    "name":"John Doe",  
    "email":"john@gmail.com",
    "password":"$2a$10$uhUIUmVWVnrBWx9rrDWhS.",
    "roles":["NORMAL"],
    "urls": [
        {
            "id":1,
            "originalUrl":"https://www.google.com",
            "shortUrl":"24WnSpXt",
            "creationDate":"2023-07-07T17:09:42.411",
            "expirationDate":" 2023-11-04T17:09:42.411"
        }
    ]
}
```
</td>
</tr>
</table>

### Congfiuration
1. Pull the docker image 

    ```bash
    docker pull surjendu104/url-shortener-images:latest
    ```
2. Run your mongodb server by running this in terminal
    ```bash 
    mongod
    ```
3. Run the container by setting enviorment variable

    ```bash
    docker run \
    -p 8080:8080 \
    -e DB_HOST=<YOUR_IP_ADDRESS> \
    -e DB_PORT=<MONGODB_PORT> \
    -e DB_COLLECTION=<MONGODB_COLLECTION_NAME> \
    -e JWT_SECRET=<JWT_SECRET> \
    -e SHORT_URL_KEY_LENGTH=<LENGTH_YOU_WANT> \
    surjendu104/url-shortener-images
    ```

    #### Sample command  
    ```bash
    docker run \
    -p 8080:8080 \
    -e DB_HOST=192.168.0.119 \
    -e DB_PORT=27017 \
    -e DB_COLLECTION=url_shortener \
    -e JWT_SECRET="afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf" \
    -e SHORT_URL_KEY_LENGTH=4 \
    surjendu104/url-shortener-images
    ```

## Resources
1. [Springboot docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
2. [Mongodb Guide](https://www.mongodb.com/docs/)
3. [Docker guide](https://docs.docker.com/)


### Fell free to raise any issue and pr !!
## That's it :)
