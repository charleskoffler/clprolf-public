using Clprolf.ArchUnitNet.Attributes;
using System.ComponentModel;
using System.Runtime.Serialization;

namespace Clprolf.Examples.WPF.Agents.Traits.SystemOriented
{
    /// <summary>
    /// This wrapper is not mandatory, and for educational purpose. You can implement INotifyPropertyChanged directly
    /// </summary>
    ///
    [ClAgent]
    [ClTrait]
    public interface IClNotifyPropertyChanged: INotifyPropertyChanged
    {
    }
}