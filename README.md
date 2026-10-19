\# Room API



A small REST API for managing meeting rooms: you can list, search, create, replace and delete rooms. Data is kept in memory, so it resets when the server restarts.



\## Endpoints



| Method | Path | Status | What it does |

|---|---|---|---|

| GET | /api/rooms | 200 | List all rooms; optional filters `minCapacity` and `keyword` |

| GET | /api/rooms/{id} | 200 / 404 | Get one room |

| POST | /api/rooms | 201 + Location | Create a room; returns the new room and its Location header |

| PUT | /api/rooms/{id} | 200 / 404 | Replace a room; 404 if it does not exist |

| DELETE | /api/rooms/{id} | 204 / 404 | Delete a room; 404 if it does not exist |



\## How to run



Requires JDK 21.



&#x20;   gradlew.bat bootRun        (Windows)

&#x20;   ./gradlew bootRun          (macOS / Linux)



The server starts on http://localhost:8080. The requests in `api.http` test every endpoint.

