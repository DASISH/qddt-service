Readme will come.

Running docker of the database:
docker run --name qddt --hostname=auth -p 127.0.0.16:5432:5432 -e POSTGRES_PASSWORD=password -e POSTGRES_DB=qddt -d postgres



Deploy
-------

gradlew build -x test

Documentation
-------------
https://dasish.github.io/qddt-service/
