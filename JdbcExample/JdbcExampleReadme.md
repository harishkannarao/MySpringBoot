# Jdbc Example
This module demonstrates accessing data using Jdbc template from within a Rest Service and covers functional testing of the application with fixtures

### Run full build

    mvn clean install

#### Test large file streaming upload using curl

Upload file

    curl --header "Content-Type: multipart/form-data" \
        -X POST "http://localhost:8180/form-submit" \
        -F "firstName=first" \
        -F "lastName=last" \
        -F "files=@$HOME/Downloads/video1.mp4;type=video/mp4" \
        -F "files=@$HOME/Downloads/video2.mp4;type=video/mp4"

Download a file

    curl -O "http://localhost:8180/files/video1.mp4"