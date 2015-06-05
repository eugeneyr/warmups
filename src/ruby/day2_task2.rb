#!/usr/bin/ruby

arr = Array.new(16) {|idx| idx ** 2}

i = 0
arr.each_slice(4) do |slice|
  puts "slice #{i + 1}", slice
  i = i.next
end