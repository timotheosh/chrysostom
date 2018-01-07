(ns chrysostom.test
  (:require [chrysostom.static.web]
            [chrysostom.handler]))

["/" [["" #function[chrysostom.handler/index-handler]] ["index.html" #function[chrysostom.handler/index-handler]] ["about.html" {:headers {"Content-type" "text/html"}, :status 200, :body "<html>\n    <head>\n        <title>Simple Template</title>\n    </head>\n    <body>\n        <h1>Sidebar</h1>\n        <div id=\"sidebar\"></div>\n        <h1>Main Text</h1>\n        <div id=\"text\">&lt;h1&gt;About this site&lt;/h1&gt;\n&lt;p&gt;\n    It is a partial web page.\n&lt;/p&gt;\n</div>\n    </body>\n\n</html>"}] ["mytest.html" {:headers {"Content-type" "text/html"}, :status 200, :body "<html>\n    <head>\n        <title>Simple Template</title>\n    </head>\n    <body>\n        <h1>Sidebar</h1>\n        <div id=\"sidebar\"></div>\n        <h1>Main Text</h1>\n        <div id=\"text\">&lt;p&gt;This is my test.&lt;/p&gt;\n</div>\n    </body>\n\n</html>"}]]]
