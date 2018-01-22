- [This is the outer section](#orgb88c682)
  - [This is an inner section](#orgd69617c)



<a id="orgb88c682"></a>

# This is the outer section


<a id="orgd69617c"></a>

## This is an inner section

Inner section body &#x2013; *with italic text*! And **bold text** too.

-   Plain List Item 1
-   Plain List Item 2

[A link to a Web site](http://eigenhombre.com)

Lisp code

```lisp
(defparameter *foo* "hello-world")

(defun foo-func (bar)
    (format t "Hello, ~A~%" bar))
```

C code

```c
#include <stdio.h>

int main(int argc, char **argv) {
    puts("Hello, World!");
    return 0;
}
```

Some Python

```python
class Foo:
    def __init__(self):
        self.var1 = "foo"

    def run(self):
        print("{}".format(self.var1))

if __name__ == "__main__":
    f = Foo()
    f.run()
```

Some shell

```shell
for x in $(find /tmp -type f -print0|xargs -0 grep -i foobar);do
    echo $x
done
```

Some Clojure

```clojure
(ns ancillary.execution-handler
    (:require [clojure.java.shell :as shell]
              [clojure.data.json :as json]))

(defn exec-sh
    "Executes and handles shell commands."
    [command]
    (let [result (shell/sh "bash" "-c" command)]
        (cond (= (get result :exit) 0) (def status 200)
              :else (def status 510))
        {:status status
         :header {"Content-Type" "application/json"}
         :body (json/write-str
              {:stdout (get result :out)
               :stderr (get result :err)})}))
```
