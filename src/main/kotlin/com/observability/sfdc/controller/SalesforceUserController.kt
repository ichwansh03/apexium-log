package com.observability.sfdc.controller

import com.observability.sfdc.dto.SalesforceUserDto
import com.observability.sfdc.service.SalesforceUserService
import org.springframework.web.bind.annotation.*

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
        return userService.getAllUsers(size, offset)
    }
}
