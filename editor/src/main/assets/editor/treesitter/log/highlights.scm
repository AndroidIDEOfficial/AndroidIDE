(begin_header) @header

(log_line
  (date) @err.date
  (time) @err.time
  (pid) @err.pid
  (tid) @err.tid
  (tag) @err.tag
  (priority) @err.priority
  (message) @err.msg
  (#match? @err.priority "E")
)

(log_line
  (date) @warn.date
  (time) @warn.time
  (pid) @warn.pid
  (tid) @warn.tid
  (tag) @warn.tag
  (priority) @warn.priority
  (message) @warn.msg
  (#match? @warn.priority "W")
)

(log_line
  (date) @info.date
  (time) @info.time
  (pid) @info.pid
  (tid) @info.tid
  (tag) @info.tag
  (priority) @info.priority
  (message) @info.msg
  (#match? @info.priority "I")
)

(log_line
  (date) @debug.date
  (time) @debug.time
  (pid) @debug.pid
  (tid) @debug.tid
  (tag) @debug.tag
  (priority) @debug.priority
  (message) @debug.msg
  (#match? @debug.priority "D")
)

(log_line
  (date) @verbose.date
  (time) @verbose.time
  (pid) @verbose.pid
  (tid) @verbose.tid
  (tag) @verbose.tag
  (priority) @verbose.priority
  (message) @verbose.msg
  (#match? @verbose.priority "V")
)

(ide_log_line
  (ide_tag) @err.tag
  (priority) @err.priority
  (message) @err.msg
  (#match? @err.priority "E")
)

(ide_log_line
  (ide_tag) @warn.tag
  (priority) @warn.priority
  (message) @warn.msg
  (#match? @warn.priority "W")
)

(ide_log_line
  (ide_tag) @info.tag
  (priority) @info.priority
  (message) @info.msg
  (#match? @info.priority "I")
)

(ide_log_line
  (ide_tag) @debug.tag
  (priority) @debug.priority
  (message) @debug.msg
  (#match? @debug.priority "D")
)

(ide_log_line
  (ide_tag) @verbose.tag
  (priority) @verbose.priority
  (message) @verbose.msg
  (#match? @verbose.priority "V")
)