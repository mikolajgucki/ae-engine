#ifndef AE_STRING_UTIL_H
#define AE_STRING_UTIL_H

#include <string>
#include <vector>

namespace ae {
    
namespace util {
    
/**
 * \brief Checks if a string starts with a prefix.
 * \param str The string.
 * \param prefix The prefix;
 * \return <code>true</code> if the string starts with the prefix,
 *   <code>false</code> otherwise.
 */
bool startsWith(const std::string& str,const std::string& prefix);
    
/** 
 * \brief Splits a string.
 * \param str The string to split.
 * \param delim The delimiter.
 * \return The split items.
 */
std::vector<std::string> split(const std::string &str,char delim);

/**
 * \brief Performs left trim.
 * \param str The string to trim.
 * \param characters The characters to trim.
 */
void ltrim(std::string &str,const std::string &characters);

/**
 * \brief Checks if a string represents an integer number.
 * \param str The string.
 * \return <code>true</code> if the string represents an integer number,
 *   <code>false</code> otherwise.
 */
bool isInt(const std::string &str);

/**
 * \brief Parses an integer number.
 * \param str The string with the integer.
 * \param value The result value.
 * \return <code>true</code> if the string represents an integer number,
 *   <code>false</code> if could not be parsed.
 */
bool parseInt(const std::string &str,int &value);

} // namespace
    
} // namespace

#endif // AE_STRING_UTIL_H