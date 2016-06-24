Readme will come.

Running docker of the database:
docker run --name qddt --hostname=auth -p 127.0.0.16:5432:5432 -e POSTGRES_PASSWORD=password -e POSTGRES_DB=qddt -d postgres

Offline dependencies
--------------------

Folder libs contains a updated version of spring-data-envers.
It can be found here: https://github.com/scav/spring-data-envers


Deploy
-------

gradlew build -x test
