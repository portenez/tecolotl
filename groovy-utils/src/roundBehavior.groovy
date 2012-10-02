/**
 * This script can be used to analyze the decimal rounding behavior in java
 * 
 * @author vgarcia
 *
 */

totalUsers = 255
divideIn = 4
percentage = 1/divideIn
notRounded = totalUsers*percentage
//users = Math.ceil(notRounded)
//users = Math.floor(notRounded)
users = Math.round(notRounded)
generatedTotal = users*divideIn
toPrint =  ['total':totalUsers, 
'percentage':percentage,
 'not-roudned':notRounded, 
 'rounded':users,
 'total': generatedTotal]

println toPrint 