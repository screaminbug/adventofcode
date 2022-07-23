package hr.tstrelar.adventofcode

class UsageException(detail: String) : Exception(
    """
    Usage: adventovcode <day> <file>
    day: a day of the challenge, for example: 1
    
    Days 1-2 currently implemented
    File structure depends on a day 
    
    What went wrong: $detail
    """
) {
}