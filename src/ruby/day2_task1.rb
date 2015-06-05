#!/usr/bin/ruby

arr = Array.new(16) {|idx| idx ** 2}

i = 0
tmpArr = nil
arr.each() do |elem|
#  puts i
  tmpArr = [] if i % 4 == 0
  tmpArr.push elem
  puts "slice #{(i + 1) / 4}", tmpArr if i %4 == 3
  i = i.next
end