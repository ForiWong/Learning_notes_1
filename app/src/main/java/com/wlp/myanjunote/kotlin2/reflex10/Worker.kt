package com.wlp.myanjunote.kotlin2.reflex10

data class Worker(val name: String, @DeserializeInterface(CompanyImpl::class) val company: Company) {
}