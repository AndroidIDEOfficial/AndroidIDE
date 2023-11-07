(begin_header) @header

(log_line
  (date) @err.date
  (time) @err.time
  (pid) @err.pid
  (tid) @err.tid
  (level) @err.level
  (tag) @err.tag
  (message) @err.msg
  (#match? @err.level "E")
)

(log_line
  (date) @warn.date
  (time) @warn.time
  (pid) @warn.pid
  (tid) @warn.tid
  (level) @warn.level
  (tag) @warn.tag
  (message) @warn.msg
  (#match? @warn.level "W")
)

(log_line
  (date) @info.date
  (time) @info.time
  (pid) @info.pid
  (tid) @info.tid
  (level) @info.level
  (tag) @info.tag
  (message) @info.msg
  (#match? @info.level "I")
)

(log_line
  (date) @debug.date
  (time) @debug.time
  (pid) @debug.pid
  (tid) @debug.tid
  (level) @debug.level
  (tag) @debug.tag
  (message) @debug.msg
  (#match? @debug.level "D")
)

(log_line
  (date) @verbose.date
  (time) @verbose.time
  (pid) @verbose.pid
  (tid) @verbose.tid
  (level) @verbose.level
  (tag) @verbose.tag
  (message) @verbose.msg
  (#match? @verbose.level "V")
)

(ide_log_line
  (ide_tag) @err.tag
  (level) @err.level
  (message) @err.msg
  (#match? @err.level "E")
)

(ide_log_line
  (ide_tag) @warn.tag
  (level) @warn.level
  (message) @warn.msg
  (#match? @warn.level "W")
)

(ide_log_line
  (ide_tag) @info.tag
  (level) @info.level
  (message) @info.msg
  (#match? @info.level "I")
)

(ide_log_line
  (ide_tag) @debug.tag
  (level) @debug.level
  (message) @debug.msg
  (#match? @debug.level "D")
)

(ide_log_line
  (ide_tag) @verbose.tag
  (level) @verbose.level
  (message) @verbose.msg
  (#match? @verbose.level "V")
)