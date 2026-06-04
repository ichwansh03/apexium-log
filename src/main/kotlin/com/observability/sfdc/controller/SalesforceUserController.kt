package com.observability.sfdc.controller

import com.observability.sfdc.domain.User
import com.observability.sfdc.dto.SalesforceUserDto
import com.observability.sfdc.service.SalesforceUserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sfdc/users")
class SalesforceUserController(
    private val userService: SalesforceUserService
) {

    @GetMapping
    fun getUsers(
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<SalesforceUserDto> {
        val offset = page * size
        return userService.getAllUsers(limit = size, offset = offset)
    }

    @GetMapping("/db")
    fun searchUsers(
        @RequestParam(required = false) name: String?,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): List<User> {
        val offset = page * size
        return userService.searchUsers(name, size, offset)
    }
}
