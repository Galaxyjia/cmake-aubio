cmake_minimum_required(VERSION 3.4.1)

project(aubio)

aux_source_directory(. DIR_SRCS)

add_subdirectory(io)
add_subdirectory(notes)
add_subdirectory(onset)
add_subdirectory(pitch)
add_subdirectory(spectral)
add_subdirectory(synth)
add_subdirectory(tempo)
add_subdirectory(temporal)
add_subdirectory(utils)

add_library(
        aubio

        SHARED
        ${DIR_SRCS}

)

#add_library(
#        pitcha
#
#        SHARED
#
#        pitch.c
#)

target_link_libraries(
    aubio
    io
    notes
    onset
    pitch
    spectral
    synth
    tempo
    temporal
    utils
)

