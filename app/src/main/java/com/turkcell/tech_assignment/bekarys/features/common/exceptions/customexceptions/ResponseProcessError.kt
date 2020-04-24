package com.turkcell.tech_assignment.bekarys.features.common.exceptions.customexceptions

import com.turkcell.tech_assignment.bekarys.features.common.exceptions.InternalException

class ResponseProcessError(message: String): InternalException(message)