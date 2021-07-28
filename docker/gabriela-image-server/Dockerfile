# We use Amazon Corretto because OpenJDK only provides Early Access builds
FROM amazoncorretto:15-alpine-jdk

# Install required dependencies
RUN apk add ffmpeg gifsicle

# JRE fails to load fonts if there are no standard fonts in the image; DejaVu is a good choice,
# see https://github.com/docker-library/openjdk/issues/73#issuecomment-207816707
# https://stackoverflow.com/a/58713958/7271796
# Also removes APK's cache
RUN apk add --update ttf-dejavu && rm -rf /var/cache/apk/*

# Copy all required files for Gabriela Image Server
COPY files /home/gabriela-image-server/

# Create folders (-p = ignore errors)
RUN mkdir -p /home/gabriela-image-server/temp/
RUN mkdir -p /home/gabriela-image-server/assets/

# Expose the port
EXPOSE 8001/tcp

# And that's it!
ENTRYPOINT ["/home/gabriela-image-server/start.sh"]
