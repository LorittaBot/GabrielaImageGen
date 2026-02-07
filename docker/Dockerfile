FROM eclipse-temurin:25-noble

# Install required dependencies
RUN apt-get update && apt-get install -y ffmpeg gifsicle libwebp7 libwebpdemux2 && apt-get clean && rm -rf /var/lib/apt/lists/*

RUN ln -s /usr/lib/x86_64-linux-gnu/libwebp.so.7 /usr/lib/x86_64-linux-gnu/libwebp.so && ln -s /usr/lib/x86_64-linux-gnu/libwebpdemux.so.2 /usr/lib/x86_64-linux-gnu/libwebpdemux.so