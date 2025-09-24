package com.familyfund.backend.dto;

public record MemberResponse(
    Long id,
    String nombre,
    String email
) {}
