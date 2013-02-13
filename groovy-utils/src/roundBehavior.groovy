/**
 * @author Victor Garcia: https://github.com/portenez
 * 
 * Very simple script to analyze the different "rounding" mechanisms in java
 * 
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