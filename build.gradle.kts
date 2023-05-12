plugins {
    id("java")
    id("application")
}

group = "cc.wybxc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("cc.wybxc.Main")
}

dependencies {
    implementation("io.github.vincenzopalazzo:material-ui-swing:1.1.4")
    implementation("com.fifesoft:rsyntaxtextarea:3.3.1")
    implementation("org.jsoup:jsoup:1.15.4")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
