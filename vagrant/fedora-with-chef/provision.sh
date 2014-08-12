#!/bin/bash
yum -y install ruby-devel gcc gcc-c++ libxml2-devel libxslt-devel autoconf
gem install chef --no-ri --no-rdoc
gem install knife-solo --no-ri --no-rdoc
#gem install berkshelf --no-ri --no-rdoc
gem install foodcritic --no-ri --no-rdoc
