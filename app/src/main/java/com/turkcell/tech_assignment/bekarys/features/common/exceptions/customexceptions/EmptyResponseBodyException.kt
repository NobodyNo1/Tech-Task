package com.turkcell.tech_assignment.bekarys.features.common.exceptions.customexceptions

import com.turkcell.tech_assignment.bekarys.features.common.exceptions.NetworkException

class EmptyResponseBodyException(message: String) : NetworkException(message)