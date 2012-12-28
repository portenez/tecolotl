println "start"
def matcher = 'jdbc:postgresql://ea-mysql1:5432/JMeterWH' =~ /jdbc:postgresql:\/\/(.+?):(\d+?)\/(.+)/

matcher.find();
def host = matcher.group(1)
def port = matcher.group(2)
def db = matcher.group(3)

println "host:${host} port:${port} db:${db}"



