# This is my build file. Read it before you run it, it will DELETE FILES.
# It is included here just to to give you an idea how to build.

clear
javac transtoba2.java -target 1.8 -Xlint
jar cvfm transtoba2-0.23.jar META-INF/MANIFEST.MF *class *ttf *dat *txt *html *java README* ??.png build.sh
rm *~
