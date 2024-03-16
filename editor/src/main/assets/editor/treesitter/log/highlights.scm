(begin_header) @header

(log_line
  (date) @err.date
  (time) @err.time
  (pid) @err.pid
  (tid) @err.tid
  (priority) @err.priority
  (tag) @err.tag
  (message) @err.msg
  (#match? @err.priority "E")
)

(log_line
  (date) @warn.date
  (time) @warn.time
  (pid) @warn.pid
  (tid) @warn.tid
  (priority) @warn.priority
  (tag) @warn.tag
  (message) @warn.msg
  (#match? @warn.priority "W")
)

(log_line
  (date) @info.date
  (time) @info.time
  (pid) @info.pid
  (tid) @info.tid
  (priority) @info.priority
  (tag) @info.tag
  (message) @info.msg
  (#match? @info.priority "I")
)

(log_line
  (date) @debug.date
  (time) @debug.time
  (pid) @debug.pid
  (tid) @debug.tid
  (priority) @debug.priority
  (tag) @debug.tag
  (message) @debug.msg
  (#match? @debug.priority "D")
)

(log_line
  (date) @verbose.date
  (time) @verbose.time
  (pid) @verbose.pid
  (tid) @verbose.tid
  (priority) @verbose.priority
  (tag) @verbose.tag
  (message) @verbose.msg
  (#match? @verbose.priority "V")
)

(ide_log_line
  (date) @err.date
  (time) @err.time
  (priority) @err.priority
  (thread_name) @err.thread_name
  (tag) @err.tag
  (message) @err.msg
  (#match? @err.priority "ERROR")
)

(ide_log_line
  (date) @warn.date
  (time) @warn.time
  (priority) @warn.priority
  (thread_name) @warn.thread_name
  (tag) @warn.tag
  (message) @warn.msg
  (#match? @warn.priority "WARN")
)

(ide_log_line
  (date) @info.date
  (time) @info.time
  (priority) @info.priority
  (thread_name) @info.thread_name
  (tag) @info.tag
  (message) @info.msg
  (#match? @info.priority "INFO")
)

(ide_log_line
  (date) @debug.date
  (time) @debug.time
  (priority) @debug.priority
  (thread_name) @debug.thread_name
  (tag) @debug.tag
  (message) @debug.msg
  (#match? @debug.priority "DEBUG")
)

(ide_log_line
  (date) @verbose.date
  (time) @verbose.time
  (priority) @verbose.priority
  (thread_name) @verbose.thread_name
  (tag) @verbose.tag
  (message) @verbose.msg
  (#match? @verbose.priority "TRACE")
)
