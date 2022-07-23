package hr.tstrelar.adventofcode

class UsageException(detail: String) : Exception(
    """
    Usage: adventovcode <day> <file>
    day: a day of the challenge, for example: 1
    Days 1-1 currently implemented
    A file should contain only integers separated by a new line
    
    What went wrong: $detail
    """
) {
}