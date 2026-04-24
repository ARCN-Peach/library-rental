package com.library.rental.application.port;

import java.time.Instant;

public interface ClockPort {

    Instant now();
}