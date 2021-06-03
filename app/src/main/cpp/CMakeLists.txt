cmake_minimum_required(VERSION 3.4.1)

add_library(fc SHARED main.cpp)
find_library(log-lib log)
target_link_libraries(fc ${log-lib})
