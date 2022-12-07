# Description

**Application features :**

- Consume zombie locations and zombie captured
- Store zombie's information in db
- Get zombies still awaiting capture that are the closest to those coordinates, ordered by distance
  by calling this endpoint GET /zombies?lat=48.872544&lon=2.332298&limit=5

## Start the application

**Prerequisites**

docker and docker-compose installed

**Start the app with docker**

```sh
docker build -t technicaltest .
docker-compose -d up
```

*First launch can take several minutes*

**Generate dummy data for test**
Call the endpoint POST /zombie/locations