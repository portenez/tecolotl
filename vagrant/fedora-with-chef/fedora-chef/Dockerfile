#Create a docker container that runs fedora and runs chef-dk
FROM fedora:20
MAINTAINER Victor Garcia <vh.garcia.valencia@gmail.com>
run yum install -y ruby-devel ruby gcc gcc-c++ libxml2-devel libxslt-devel autoconf gecode patch make
run gem install chef --no-ri --no-rdoc
run gem install knife-solo --no-ri --no-rdoc
run gem install berkshelf --no-ri --no-rdoc
run gem install foodcritic --no-ri --no-rdoc
