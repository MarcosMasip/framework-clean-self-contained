#!/bin/sh
# Apache Maven Wrapper startup script (simplified, tailored for project self-containment)
set -e
BASEDIR=$(cd "$(dirname "$0")"; pwd -P)
WRAPPER_DIR="$BASEDIR/.mvn/wrapper"
JAR="$WRAPPER_DIR/maven-wrapper.jar"
PROPS="$WRAPPER_DIR/maven-wrapper.properties"

download() {
  url="$1"
  dest="$2"
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$url" -o "$dest"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "$url" -O "$dest"
  else
    echo "Need curl or wget to download $url" >&2; exit 1
  fi
}

if [ ! -f "$PROPS" ]; then
  echo "distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip" > "$PROPS"
fi

DIST_URL=$(grep -E '^distributionUrl=' "$PROPS" | cut -d= -f2-)
MAVEN_HOME="$WRAPPER_DIR/dist"
DIST_ARCHIVE="$WRAPPER_DIR/$(basename "$DIST_URL")"

if [ ! -f "$JAR" ]; then
  download "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar" "$JAR"
fi

if [ ! -d "$MAVEN_HOME/apache-maven" ]; then
  mkdir -p "$MAVEN_HOME"
  download "$DIST_URL" "$DIST_ARCHIVE"
  unzip -q "$DIST_ARCHIVE" -d "$MAVEN_HOME"
  mv "$MAVEN_HOME"/apache-maven-* "$MAVEN_HOME"/apache-maven
fi

JAVA_EXE="${JAVA_HOME:+$JAVA_HOME/bin/}java"
if ! command -v "$JAVA_EXE" >/dev/null 2>&1; then
  JAVA_EXE=java
fi

exec "$JAVA_EXE" -Dmaven.multiModuleProjectDirectory="$BASEDIR" \
  -cp "$JAR" org.apache.maven.wrapper.MavenWrapperMain "$@"
